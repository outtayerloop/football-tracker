using FootTracker.Api.Domain;
using FootTracker.Api.Domain.Dto;
using FootTracker.Api.Domain.Models;
using System;

namespace FootTracker.Api.Converters
{
    public static class CardConverter
    {
        public static CardDto ConvertToDto(Card cardToConvert)
        {
            if (cardToConvert == null)
                return null;

            return new CardDto()
            {
                Type = Enum.GetName(typeof(CardType), cardToConvert.Type),
                Moment = cardToConvert.Moment.ToString(),
                PlayerId = cardToConvert.PlayerId,
                GameId = cardToConvert.GameId
            };
        }

        public static Card ConvertToModel(CardDto cardToConvert)
        {
            if (cardToConvert == null)
                return null;

            return new Card()
            {
                Type = (CardType)Enum.Parse(typeof(CardType), cardToConvert.Type),
                Moment = TimeSpan.Parse(cardToConvert.Moment),
                PlayerId = cardToConvert.PlayerId,
                GameId = cardToConvert.GameId
            };
        }
    }
}
