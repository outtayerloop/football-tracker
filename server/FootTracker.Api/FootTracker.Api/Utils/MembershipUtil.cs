using FootTracker.Api.Domain.Models;
using System.Collections.Generic;
using System.Linq;

namespace FootTracker.Api.Utils
{
    public static class MembershipUtil
    {
        public static List<Team> GetAllTeamsFromMultipleMemberships(List<Membership> memberships)
        {
            var teams = new List<Team>();
            memberships.ForEach(membership =>
            {
                Team foundTeam = teams.FirstOrDefault(team => team.Id == membership.Team.Id);
                if (foundTeam == null)
                    AddRetrievedTeamToList(membership, teams);
            });
            return teams;
        }

        public static Membership GetMembershipFromData(Team team, Player player)
        {
            return new Membership
            {
                PlayerId = player.Id,
                TeamId = team.Id
            };
        }

        private static void AddRetrievedTeamToList(Membership membership, List<Team> teams)
        {
            membership.Team.SetTeamPlayers();
            teams.Add(new Team()
            {
                Id = membership.Team.Id,
                Players = membership.Team.Players,
                Name = membership.Team.Name
            });
        }
    }
}
