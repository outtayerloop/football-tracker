using FootTracker.Api.Domain.Dto;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FootTracker.Api.Services.GameUpdate
{
    public interface IGameUpdateService
    {
        Task<ActionResult<GameDto>> UpdateGame(GameDto gameData);
        Task<ActionResult<GameAttachmentDto>> CreateAttachment(GameAttachmentDto attachmentDto);
        Task<ActionResult<CardDto>> CreateCard(CardDto cardDto);
        Task<ActionResult<GoalDto>> CreateGoal(GoalDto goalDto);
    }
}
