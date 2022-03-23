using FootTracker.Api.Domain.Dto;
using FootTracker.Api.Domain.Models;
using FootTracker.Api.Repositories;
using FootTracker.Api.Utils;
using System.Collections.Generic;

namespace FootTracker.Api.Services.GameReview
{
    public class GameReviewService : IGameReviewService
    {

        private readonly IGameRepository _gameRepository;

        public GameReviewService(IGameRepository gameRepository)
            => _gameRepository = gameRepository;

        public List<GameDto> GetAllGames()
        {
            List<Game> games = _gameRepository.GetAll();
            games.ForEach(soccerMatch => {
                soccerMatch.TrackedTeam.SetTeamPlayers();
                soccerMatch.OpponentTeam.SetTeamPlayers();
            });
            return ConversionUtil.GetAllDtosFromModels<Game, GameDto>(games);
        }
    }
}
