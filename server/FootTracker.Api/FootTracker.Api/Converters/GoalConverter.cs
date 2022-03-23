using FootTracker.Api.Domain.Dto;
using FootTracker.Api.Domain.Models;
using System;

namespace FootTracker.Api.Converters
{
    public static class GoalConverter
    {
        public static GoalDto ConvertToDto(Goal goalToConvert)
        {
            if (goalToConvert == null)
                return null;

            return new GoalDto()
            {
                PlayerId = goalToConvert.PlayerId,
                GameId = goalToConvert.GameId,
                Moment = goalToConvert.Moment.ToString()
            };
        }

        public static Goal ConvertToModel(GoalDto goalToConvert)
        {
            if (goalToConvert == null)
                return null;

            return new Goal()
            {
                PlayerId = goalToConvert.PlayerId,
                GameId = goalToConvert.GameId,
                Moment = TimeSpan.Parse(goalToConvert.Moment)
            };
        }
    }
}
