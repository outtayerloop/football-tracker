using FootTracker.Api.Converters;
using FootTracker.Api.Domain.Dto;
using FootTracker.Api.Domain.Models;
using System;
using System.Collections.Generic;

namespace FootTracker.Api.Utils
{
    public static class ConversionUtil
    {
        public static List<TDto> GetAllDtosFromModels<TModel, TDto>(List<TModel> models) where TModel : BaseModel where TDto : BaseDto
        {
            var convertedDtoList = new List<TDto>();
            models.ForEach(modelToConvert => {
                BaseDto convertedDto = GetDtoFromModel(modelToConvert);
                convertedDtoList.Add(convertedDto as TDto);
            });
            return convertedDtoList;
        }

        public static BaseModel GetModelFromDto<TDto>(TDto dtoToConvert) where TDto : BaseDto
        {
            return dtoToConvert switch
            {
                GameDto _ => GameConverter.ConvertToModel(dtoToConvert as GameDto),
                CardDto _ => CardConverter.ConvertToModel(dtoToConvert as CardDto),
                GoalDto _ => GoalConverter.ConvertToModel(dtoToConvert as GoalDto),
                GameAttachmentDto _=> AttachmentConverter.ConvertToModel(dtoToConvert as GameAttachmentDto),             
                _ => throw new NotImplementedException("No model converter found for given dto")
            };
        }

        public static List<TrackedPlayerDto> GetAllTrackedPlayersFromModels(List<Player> trackedPlayersToConvert)
        {
            var convertedTrackedPlayers = new List<TrackedPlayerDto>();
            if(trackedPlayersToConvert != null)
            {
                trackedPlayersToConvert.ForEach(trackedPlayerToConvert =>
                {
                    TrackedPlayerDto convertedTrackedPlayer = trackedPlayerToConvert != null
                        ? PlayerConverter.ConvertToTrackedDto(trackedPlayerToConvert)
                        : null;
                    convertedTrackedPlayers.Add(convertedTrackedPlayer);
                });
            }
            return convertedTrackedPlayers;
        }

        public static List<GenericPlayerDto> GetAllGenericPlayersFromModels(List<Player> untrackedPlayersToConvert)
        {
            var convertedGenericPlayers = new List<GenericPlayerDto>();
            if(untrackedPlayersToConvert != null)
            {
                untrackedPlayersToConvert.ForEach(untrackedPlayerToConvert =>
                {
                    GenericPlayerDto convertedGenericPlayer = untrackedPlayerToConvert != null
                        ? PlayerConverter.ConvertToUntrackedDto(untrackedPlayerToConvert)
                        : null;
                    convertedGenericPlayers.Add(convertedGenericPlayer);
                });
            }
            return convertedGenericPlayers;
        }

        public static List<Player> GetAllTrackedPlayersFromDtos(List<TrackedPlayerDto> trackedPlayersToConvert)
        {
            var convertedTrackedPlayers = new List<Player>();
            if(trackedPlayersToConvert != null)
            {
                trackedPlayersToConvert.ForEach(trackedPlayerToConvert =>
                {
                    Player convertedTrackedPlayer = trackedPlayerToConvert != null
                        ? PlayerConverter.ConvertToTrackedModel(trackedPlayerToConvert)
                        : null;
                    convertedTrackedPlayers.Add(convertedTrackedPlayer);
                });
            }
            return convertedTrackedPlayers;
        }


        public static List<Player> GetAllGenericPlayersFromDtos(List<GenericPlayerDto> untrackedPlayersToConvert)
        {
            var convertedGenericPlayers = new List<Player>();
            if(untrackedPlayersToConvert != null)
            {
                untrackedPlayersToConvert.ForEach(untrackedPlayerToConvert =>
                {
                    Player convertedGenericPlayer = untrackedPlayerToConvert != null
                        ? PlayerConverter.ConvertToUntrackedModel(untrackedPlayerToConvert)
                        : null;
                    convertedGenericPlayers.Add(convertedGenericPlayer);
                });
            }
            return convertedGenericPlayers;
        }

        public static List<TModel> GetAllModelsFromDtos<TDto, TModel>(List<TDto> dtos) where TModel : BaseModel where TDto : BaseDto
        {
            var convertedModelList = new List<TModel>();
            if(dtos != null)
            {
                dtos.ForEach(dtoToConvert => {
                    BaseModel convertedModel = dtoToConvert != null
                        ? GetModelFromDto(dtoToConvert)
                        : null;
                    convertedModelList.Add(convertedModel as TModel);
                });
            }
            return convertedModelList;
        }

        public static BaseDto GetDtoFromModel<TModel>(TModel modelToConvert) where TModel : BaseModel
        {
            return modelToConvert switch
            {
                Championship _ => ChampionshipConverter.ConvertToDto(modelToConvert as Championship),
                Referee _ => RefereeConverter.ConvertToDto(modelToConvert as Referee),
                Team _ => TeamConverter.ConvertToUntrackedDto(modelToConvert as Team),
                Player _ => PlayerConverter.ConvertToUntrackedDto(modelToConvert as Player),
                Game _ => GameConverter.ConvertToDto(modelToConvert as Game),
                GameAttachment _ => AttachmentConverter.ConvertToDto(modelToConvert as GameAttachment),
                Card _ => CardConverter.ConvertToDto(modelToConvert as Card),
                Goal _ => GoalConverter.ConvertToDto(modelToConvert as Goal),
                _ => throw new NotImplementedException("No dto converter found for given model")
            };
        }
    }
}
