using FootTracker.Api.Domain.Dto;
using System.Collections.Generic;

namespace FootTracker.Api.Services.GameReview
{
    public interface IGameReviewService
    {
        List<GameDto> GetAllGames();
    }
}
