using FootTracker.Api.Database;
using FootTracker.Api.Domain.Models;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;

namespace FootTracker.Api.Repositories
{
    public class GameRepository : FootTrackerRepository<Game>, IGameRepository
    {
        public GameRepository(FootTrackerDbContext dbContext) : base(dbContext) { }

        public override List<Game> GetAll()
        {
            return _dbContext.Games
                .Include(game => game.TrackedTeam)
                .ThenInclude(trackedTeam => trackedTeam.Memberships)
                .ThenInclude(membership => membership.Player)
                .Include(game => game.OpponentTeam)
                .ThenInclude(opponentTeam => opponentTeam.Memberships)
                .ThenInclude(membership => membership.Player)
                .Include(game => game.Championship)
                .Include(game => game.Referee)
                .Include(game => game.Goals)
                .Include(game => game.Cards)
                .ToList();
        }
    }
}
