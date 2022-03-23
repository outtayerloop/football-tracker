using FootTracker.Api.Domain.Models;
using FootTracker.Api.Utils;
using System.Collections.Generic;

namespace FootTracker.Api.Domain.Dto
{
    public class TrackedPlayerDto : GenericPlayerDto
    {
        public List<CardDto> Cards { get; set; }

        public List<GoalDto> Goals { get; set; }

        public void SetCardsFromModelList(List<Card> cardsToConvert)
        {
            Cards = cardsToConvert == null
                ? null
                : ConversionUtil.GetAllDtosFromModels<Card, CardDto>(cardsToConvert);
        }

        public void SetGoalsFromModelList(List<Goal> goalsToConvert)
        {
            Goals = goalsToConvert == null
                ? null
                : ConversionUtil.GetAllDtosFromModels<Goal, GoalDto>(goalsToConvert);
        }
    }
}
