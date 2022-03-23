using FootTracker.Api.Database;
using FootTracker.Api.Domain.Models;
using System.Collections.Generic;
using System.Linq;

namespace FootTracker.Api.Repositories
{
    public class GoalRepository : FootTrackerRepository<Goal>, IGoalRepository
    {
        public GoalRepository(FootTrackerDbContext dbContext) : base(dbContext) { }
    }
}
