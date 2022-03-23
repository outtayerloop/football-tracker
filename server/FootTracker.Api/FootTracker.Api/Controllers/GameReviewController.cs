using FootTracker.Api.Domain.Dto;
using FootTracker.Api.Services.GameReview;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;

namespace FootTracker.Api.Controllers
{
    [Route("[controller]")]
    public class GameReviewController : ControllerBase
    {

        private readonly IGameReviewService _gameReviewService;

        public GameReviewController(IGameReviewService reviewService)
            => _gameReviewService = reviewService;

        [HttpGet("games")]
        public ActionResult<List<GameDto>> GetAllPlayers()
            => _gameReviewService.GetAllGames();
    }
}
