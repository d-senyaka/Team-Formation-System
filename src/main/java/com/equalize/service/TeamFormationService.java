package com.equalize.service;

import com.equalize.model.Team;

import java.util.stream.Collectors;


import com.equalize.model.Participant;
import com.equalize.model.enums.GameType;
import com.equalize.model.enums.PersonalityType;
import com.equalize.model.enums.RoleType;

import java.util.*;

/**
 * Handles the full team formation pipeline.
 * Step 1: Bucket Creation - grouping participants by major characteristics.
 */
public class TeamFormationService {


    /**
     * Sort each bucket (List<Participant>) by skill level in descending order.
     */
    public void sortBucketsBySkill(Map<?, List<Participant>> bucketMap) {
        for (List<Participant> list : bucketMap.values()) {
            list.sort((a, b) -> Integer.compare(b.getSkillLevel(), a.getSkillLevel()));
        }
    }

    /**
     * Bucket participants by their preferred role.
     */
    public Map<RoleType, List<Participant>> bucketByRole(List<Participant> participants) {
        Map<RoleType, List<Participant>> map = new EnumMap<>(RoleType.class);

        for (RoleType role : RoleType.values()) {
            map.put(role, new ArrayList<>());
        }

        for (Participant p : participants) {
            if (p.getPreferredRole() != null) {
                map.get(p.getPreferredRole()).add(p);
            }
        }

        return map;
    }

    /**
     * Bucket participants by preferred game.
     */
    public Map<GameType, List<Participant>> bucketByGame(List<Participant> participants) {
        Map<GameType, List<Participant>> map = new EnumMap<>(GameType.class);

        for (GameType game : GameType.values()) {
            map.put(game, new ArrayList<>());
        }

        for (Participant p : participants) {
            if (p.getPreferredGame() != null) {
                map.get(p.getPreferredGame()).add(p);
            }
        }

        return map;
    }

    /**
     * Bucket participants by personality type.
     */
    public Map<PersonalityType, List<Participant>> bucketByPersonality(List<Participant> participants) {
        Map<PersonalityType, List<Participant>> map = new EnumMap<>(PersonalityType.class);

        for (PersonalityType type : PersonalityType.values()) {
            map.put(type, new ArrayList<>());
        }

        for (Participant p : participants) {
            if (p.getPersonalityType() != null) {
                map.get(p.getPersonalityType()).add(p);
            }
        }

        return map;
    }

    /**
     * Phase 1:
     * Create initial teams and distribute participants with a role-first strategy.
     * This pass focuses on spreading roles across teams.
     */
    public List<Team> formInitialTeamsByRole(List<Participant> participants, int teamSize) {
        if (participants == null || participants.isEmpty()) {
            return Collections.emptyList();
        }

        // 1) Create empty teams based on team size
        List<Team> teams = initializeTeams(teamSize, participants.size());

        // 2) Build and sort role buckets
        Map<RoleType, List<Participant>> roleBuckets = bucketByRole(participants);
        sortBucketsBySkill(roleBuckets); // highest skill first in each role

        // 3) Distribute each role bucket across teams (round-robin)
        distributeRolesAcrossTeams(roleBuckets, teams, teamSize);

        return teams;
    }

    /**
     * Create enough Team objects to accommodate all participants.
     */
    private List<Team> initializeTeams(int teamSize, int totalParticipants) {
        int numTeams = (int) Math.ceil((double) totalParticipants / teamSize);
        if (numTeams <= 0) {
            numTeams = 1;
        }

        List<Team> teams = new ArrayList<>();
        for (int i = 1; i <= numTeams; i++) {
            teams.add(new Team("Team " + i));
        }

        // Shuffle to avoid always filling Team 1, then Team 2, etc.
        Collections.shuffle(teams);

        return teams;
    }

    /**
     * Distribute participants from each role bucket into teams in a round-robin fashion.
     * This tends to spread roles and skill more evenly across teams.
     */
    private void distributeRolesAcrossTeams(Map<RoleType, List<Participant>> roleBuckets,
                                            List<Team> teams,
                                            int teamSize) {

        int numTeams = teams.size();

        for (RoleType role : RoleType.values()) {
            List<Participant> bucket = roleBuckets.get(role);
            if (bucket == null || bucket.isEmpty()) {
                continue;
            }

            int teamIndex = 0;

            for (Participant p : bucket) {
                // Find the next team that still has space
                int tries = 0;
                while (tries < numTeams) {
                    Team targetTeam = teams.get(teamIndex);
                    if (targetTeam.size() < teamSize) {
                        targetTeam.addMember(p);
                        teamIndex = (teamIndex + 1) % numTeams;
                        break;
                    } else {
                        teamIndex = (teamIndex + 1) % numTeams;
                        tries++;
                    }
                }
                // If all teams are full, we just stop assigning (shouldn't happen if numTeams computed correctly)
            }
        }
    }

    /**
     * Calculate the global average skill across all participants.
     * Used as a reference when balancing team strength.
     */
    public double calculateGlobalAverageSkill(List<Participant> participants) {
        if (participants == null || participants.isEmpty()) {
            return 0.0;
        }
        return participants.stream()
                .mapToInt(Participant::getSkillLevel)
                .average()
                .orElse(0.0);
    }


    /**
     * Compute a penalty score if we add participant p to team t.
     * Lower penalty = better fit.
     *
     * We penalise things like:
     *  - Overfilling the team
     *  - Too many of the same game
     *  - Poor personality mix
     *  - Skill average drifting far from global average (heavily weighted)
     */
    public double computePenaltyIfAdded(Team team,
                                        Participant p,
                                        int teamSize,
                                        double globalAverageSkill) {

        // Hard block: if this would overfill the team, give a huge penalty.
        if (team.size() >= teamSize) {
            return 1_000_000; // effectively "impossible"
        }

        // Simulate team members after adding p
        List<Participant> tempMembers = new ArrayList<>(team.getMembers());
        tempMembers.add(p);

        // --- Game diversity: max 2 of same game ---
        long sameGameCount = tempMembers.stream()
                .filter(m -> m.getPreferredGame() == p.getPreferredGame())
                .count();

        double gamePenalty = 0;
        if (sameGameCount > 2) {
            gamePenalty += 5 * (sameGameCount - 2); // more than 2 → rising penalty
        }

        // --- Personality mix ---
        long leaders = tempMembers.stream()
                .filter(m -> m.getPersonalityType() == PersonalityType.LEADER)
                .count();
        long thinkers = tempMembers.stream()
                .filter(m -> m.getPersonalityType() == PersonalityType.THINKER)
                .count();
        long balanced = tempMembers.stream()
                .filter(m -> m.getPersonalityType() == PersonalityType.BALANCED)
                .count();

        double personalityPenalty = 0;

        // Prefer at least 1 leader
        if (leaders == 0) personalityPenalty += 3;
        // Too many leaders? (e.g. >2)
        if (leaders > 2) personalityPenalty += 4 * (leaders - 2);

        // Prefer at least 1 thinker
        if (thinkers == 0) personalityPenalty += 2;

        // Prefer at least 1 balanced
        if (balanced == 0) personalityPenalty += 2;

        // --- Skill balance vs global average (MORE IMPORTANT NOW) ---
        double teamAverageSkill = tempMembers.stream()
                .mapToInt(Participant::getSkillLevel)
                .average()
                .orElse(globalAverageSkill);

        double skillDiff = Math.abs(teamAverageSkill - globalAverageSkill);

        // Increase weight: skill fairness matters more (3x)
        double skillPenalty = 3 * skillDiff;

        // Total penalty is a weighted sum
        return gamePenalty + personalityPenalty + skillPenalty;
    }



    /**
     * Compute the penalty of the current team configuration.
     * Useful when comparing two teams or considering swaps.
     * Skill balance is weighted higher to avoid extreme teams.
     */
    public double computeTeamPenalty(Team team, double globalAverageSkill) {
        List<Participant> members = team.getMembers();
        if (members.isEmpty()) return 100; // empty team is not useful

        long leaders = members.stream()
                .filter(m -> m.getPersonalityType() == PersonalityType.LEADER)
                .count();
        long thinkers = members.stream()
                .filter(m -> m.getPersonalityType() == PersonalityType.THINKER)
                .count();
        long balanced = members.stream()
                .filter(m -> m.getPersonalityType() == PersonalityType.BALANCED)
                .count();

        double personalityPenalty = 0;
        if (leaders == 0) personalityPenalty += 3;
        if (leaders > 2) personalityPenalty += 4 * (leaders - 2);
        if (thinkers == 0) personalityPenalty += 2;
        if (balanced == 0) personalityPenalty += 2;

        // Game diversity penalty: games with >2 members
        Map<GameType, Long> gameCounts = members.stream()
                .filter(m -> m.getPreferredGame() != null)
                .collect(Collectors.groupingBy(
                        Participant::getPreferredGame,
                        Collectors.counting()
                ));

        double gamePenalty = 0;
        for (long count : gameCounts.values()) {
            if (count > 2) {
                gamePenalty += 5 * (count - 2);
            }
        }

        double avgSkill = members.stream()
                .mapToInt(Participant::getSkillLevel)
                .average()
                .orElse(globalAverageSkill);

        double skillDiff = Math.abs(avgSkill - globalAverageSkill);

        // Increase weight of skill difference here as well (3x)
        double skillPenalty = 3 * skillDiff;

        return personalityPenalty + gamePenalty + skillPenalty;
    }






    /**
     * Full team formation pipeline:
     *  1) Role-first distribution
     *  2) Rebalancing using penalty-based swaps
     */
    public List<Team> formBalancedTeams(List<Participant> participants, int teamSize) {
        if (participants == null || participants.isEmpty()) {
            return Collections.emptyList();
        }

        // Step 1: initial teams (role-focused)
        List<Team> teams = formInitialTeamsByRole(participants, teamSize);

        // Step 2: compute global average skill
        double globalAverageSkill = calculateGlobalAverageSkill(participants);

        // Step 3: rebalance teams to reduce penalties
        rebalanceTeams(teams, globalAverageSkill, teamSize, 30);

        return teams;
    }


    /**
     * Try to improve team balance using simple swap-based optimisation.
     * For a limited number of iterations:
     *  - find the worst (highest penalty) team
     *  - find the best (lowest penalty) team
     *  - try swapping members between them to reduce total penalty
     */
    private void rebalanceTeams(List<Team> teams,
                                double globalAverageSkill,
                                int teamSize,
                                int maxIterations) {

        if (teams == null || teams.size() < 2) {
            return;
        }

        for (int iter = 0; iter < maxIterations; iter++) {

            // 1) Compute penalties for all teams
            Team worstTeam = null;
            Team bestTeam = null;
            double worstPenalty = Double.NEGATIVE_INFINITY;
            double bestPenalty = Double.POSITIVE_INFINITY;

            for (Team team : teams) {
                double penalty = computeTeamPenalty(team, globalAverageSkill);

                if (penalty > worstPenalty) {
                    worstPenalty = penalty;
                    worstTeam = team;
                }
                if (penalty < bestPenalty) {
                    bestPenalty = penalty;
                    bestTeam = team;
                }
            }

            if (worstTeam == null || bestTeam == null || worstTeam == bestTeam) {
                return; // nothing to optimise
            }

            // 2) Try all pairwise swaps between worstTeam and bestTeam
            double currentTotalPenalty =
                    computeTeamPenalty(worstTeam, globalAverageSkill) +
                            computeTeamPenalty(bestTeam, globalAverageSkill);

            double bestImprovement = 0;
            Participant bestA = null;
            Participant bestB = null;

            List<Participant> worstMembers = new ArrayList<>(worstTeam.getMembers());
            List<Participant> bestMembers = new ArrayList<>(bestTeam.getMembers());

            for (Participant a : worstMembers) {
                for (Participant b : bestMembers) {

                    // Simulate swap
                    worstTeam.getMembers().remove(a);
                    bestTeam.getMembers().remove(b);

                    worstTeam.addMember(b);
                    bestTeam.addMember(a);

                    double newTotalPenalty =
                            computeTeamPenalty(worstTeam, globalAverageSkill) +
                                    computeTeamPenalty(bestTeam, globalAverageSkill);

                    double improvement = currentTotalPenalty - newTotalPenalty;

                    // Revert swap
                    worstTeam.getMembers().remove(b);
                    bestTeam.getMembers().remove(a);

                    worstTeam.addMember(a);
                    bestTeam.addMember(b);

                    if (improvement > bestImprovement) {
                        bestImprovement = improvement;
                        bestA = a;
                        bestB = b;
                    }
                }
            }

            // 3) If we found an improving swap, apply it
            if (bestImprovement > 0 && bestA != null && bestB != null) {
                worstTeam.getMembers().remove(bestA);
                bestTeam.getMembers().remove(bestB);

                worstTeam.addMember(bestB);
                bestTeam.addMember(bestA);
            } else {
                // No improving swap found - stop early
                return;
            }
        }
    }



}


