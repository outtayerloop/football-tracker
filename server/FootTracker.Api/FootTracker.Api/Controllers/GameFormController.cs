using FootTracker.Api.Domain.Dto;
using FootTracker.Api.Services;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace FootTracker.Api.Controllers
{
    [Route("[controller]")]
    public class GameFormController : ControllerBase
    {
        private readonly IGameFormService _gameFormService;

        public GameFormController(IGameFormService gameFormService)
            => _gameFormService = gameFormService;

        [HttpGet("championships")]
        public ActionResult<List<ChampionshipDto>> GetAllChampionships()
            => _gameFormService.GetAllChampionships();

        [HttpGet("referees")]
        public ActionResult<List<RefereeDto>> GetAllReferees()
            => _gameFormService.GetAllReferees();

        [HttpGet("teams")]
        public ActionResult<List<UntrackedTeamDto>> GetAllTeams()
            => _gameFormService.GetAllTeams();

        [HttpGet("players")]
        public ActionResult<List<GenericPlayerDto>> GetAllPlayers()
            => _gameFormService.GetAllPlayers();

        [HttpPost("games")]
        public async Task<ActionResult<GameDto>> CreateGame([FromBody] GameDto gameData)
            => await _gameFormService.CreateGame(gameData);

    }
}
