using FootTracker.Api.Domain.Dto;
using FootTracker.Api.Utils;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace FootTracker.Api.Domain.Models
{
    public class Player : BaseModel
    {
        public string FullName { get; set; }

        [EnumDataType(typeof(PlayerPosition))]
        public PlayerPosition Position { get; set; }

        public ICollection<Membership> Memberships { get; set; }

        public ICollection<Card> Cards { get; set; }

        public ICollection<Goal> Goals { get; set; }

        public void SetCommonMembersFromDto(GenericPlayerDto playerToConvert)
        {
            Id = playerToConvert.Id;
            FullName = playerToConvert.Name;
            Position = (PlayerPosition)Enum.Parse(typeof(PlayerPosition), playerToConvert.Position);
        }

        public void SetCardsFromDtoList(List<CardDto> cardsToConvert)
        {
            Cards = cardsToConvert == null
                ? null
                : ConversionUtil.GetAllModelsFromDtos<CardDto, Card>(cardsToConvert);
        }

        public void SetGoalsFromDtoList(List<GoalDto> goalsToConvert)
        {
            Goals = goalsToConvert == null
                ? null
                : ConversionUtil.GetAllModelsFromDtos<GoalDto, Goal>(goalsToConvert);
        }
    }
}
