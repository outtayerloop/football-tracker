using FootTracker.Api.Domain.Dto;
using FootTracker.Api.Domain.Models;
using System;

namespace FootTracker.Api.Converters
{
    public static class GameConverter
    {

        private static readonly string DATETIME_FORMAT = "dd/MM/yyyy HH:mm";

        public static GameDto ConvertToDto(Game gameToConvert)
        {
            if (gameToConvert == null)
                return null;

            return new GameDto()
            {
                Id = gameToConvert.Id,
                DateTime = gameToConvert.DateTime.ToString(DATETIME_FORMAT),
                Address = gameToConvert.Address,
                IsValidated = gameToConvert.IsValidated,
                TrackedTeamScore = gameToConvert.TrackedTeamScore,
                OpponentScore = gameToConvert.OpponentScore,
                OutCount = gameToConvert.OutCount,
                PenaltyCount = gameToConvert.PenaltyCount,
                FreeKickCount = gameToConvert.FreeKickCount,
                CornerCount = gameToConvert.CornerCount,
                OccasionCount = gameToConvert.OccasionCount,
                FaultCount = gameToConvert.FaultCount,
                StopCount = gameToConvert.StopCount,
                OffsideCount = gameToConvert.OffsideCount,
                OverTime = gameToConvert.OverTime.ToString(),
                TrackedTeamPossessionCount = gameToConvert.TrackedTeamPossessionCount,
                OpponentTeamPossessionCount = gameToConvert.OpponentTeamPossessionCount,
                TrackedTeam = TeamConverter.ConvertToTrackedDto(gameToConvert.TrackedTeam),
                OpponentTeam = TeamConverter.ConvertToUntrackedDto(gameToConvert.OpponentTeam),
                Referee = RefereeConverter.ConvertToDto(gameToConvert.Referee),
                Championship = ChampionshipConverter.ConvertToDto(gameToConvert.Championship)
            };
        }

        public static Game ConvertToModel(GameDto gameToConvert)
        {
            if (gameToConvert == null)
                return null;

            return new Game()
            {
                Id = gameToConvert.Id,
                DateTime = DateTime.Parse(gameToConvert.DateTime),
                Address = gameToConvert.Address,
                IsValidated = gameToConvert.IsValidated,
                TrackedTeamScore = gameToConvert.TrackedTeamScore,
                OpponentScore = gameToConvert.OpponentScore,
                OutCount = gameToConvert.OutCount,
                PenaltyCount = gameToConvert.PenaltyCount,
                FreeKickCount = gameToConvert.FreeKickCount,
                CornerCount = gameToConvert.CornerCount,
                OccasionCount = gameToConvert.OccasionCount,
                FaultCount = gameToConvert.FaultCount,
                StopCount = gameToConvert.StopCount,
                OffsideCount = gameToConvert.OffsideCount,
                OverTime = Game.GetOverTimeFromText(gameToConvert.OverTime),
                TrackedTeamPossessionCount = gameToConvert.TrackedTeamPossessionCount,
                OpponentTeamPossessionCount = gameToConvert.OpponentTeamPossessionCount,
                TrackedTeam = TeamConverter.ConvertToTrackedModel(gameToConvert.TrackedTeam),
                TrackedTeamId = gameToConvert.TrackedTeam.Id,
                OpponentTeam = TeamConverter.ConvertToUntrackedModel(gameToConvert.OpponentTeam),
                OpponentTeamId = gameToConvert.OpponentTeam.Id,
                Referee = RefereeConverter.ConvertToModel(gameToConvert.Referee),
                RefereeId = gameToConvert.Referee.Id,
                Championship = ChampionshipConverter.ConvertToModel(gameToConvert.Championship),
                ChampionshipId = gameToConvert.Id
            };
        }
    }
}
