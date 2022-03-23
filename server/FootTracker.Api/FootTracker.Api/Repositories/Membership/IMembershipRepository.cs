using FootTracker.Api.Domain.Models;
using System.Collections.Generic;

namespace FootTracker.Api.Repositories
{
    public interface IMembershipRepository : IFootTrackerRepository<Membership>
    {
        List<Player> GetTeamPlayersById(int teamId);
    }
}
