using FootTracker.Api.Database;
using FootTracker.Api.Domain.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;

namespace FootTracker.Api.Repositories
{
    public class MembershipRepository : FootTrackerRepository<Membership>, IMembershipRepository
    {
        public MembershipRepository(FootTrackerDbContext dbContext) : base(dbContext) { }

        public override List<Membership> GetAll()
        {
            return _dbContext.Memberships
                .Include(membership => membership.Team)
                .Include(membership => membership.Player)
                .ToList();
        }

        public List<Player> GetTeamPlayersById(int teamId)
        {
            return _dbContext.Memberships
               .Where(membership => membership.TeamId == teamId)
               .Select(membership => membership.Player)
               .ToList();
        }
    }
}
