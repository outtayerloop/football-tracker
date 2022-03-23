using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using FootTracker.Api.Domain.Dto;
using FootTracker.Api.Services.GameUpdate;
using Microsoft.AspNetCore.Mvc;


namespace FootTracker.Api.Controllers
{
    [Route("[controller]")]
    public class GameUpdateController : ControllerBase
    {
        private IGameUpdateService _gameUpdateService;

        public GameUpdateController(IGameUpdateService gameUpdateService)
            => _gameUpdateService = gameUpdateService;

        [HttpPost("games")]
        public async Task<ActionResult<GameDto>> UpdateGame([FromBody] GameDto gameData)
           => await _gameUpdateService.UpdateGame(gameData);

        [HttpPost("attachment")]
        public async Task<ActionResult<GameAttachmentDto>> CreateAttachment([FromBody] GameAttachmentDto gameAttachmentData)
           => await _gameUpdateService.CreateAttachment(gameAttachmentData);

        [HttpPost("card")]
        public async Task<ActionResult<CardDto>> CreateCard([FromBody] CardDto cardData)
           => await _gameUpdateService.CreateCard(cardData);

        [HttpPost("goal")]
        public async Task<ActionResult<GoalDto>> CreateGoal([FromBody] GoalDto goalData)
           => await _gameUpdateService.CreateGoal(goalData);
    }
}
