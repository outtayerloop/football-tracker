using FootTracker.Api.Domain.Dto;
using FootTracker.Api.Domain.Models;
using FootTracker.Api.Repositories;
using FootTracker.Api.Services.CreationChecker;
using FootTracker.Api.Utils;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FootTracker.Api.Services.GameUpdate
{
    public class GameUpdateService : IGameUpdateService
    {
        private readonly IGameRepository _gameRepository;
        private readonly IAttachmentRepository _attachmentRepository;
        private readonly ICardRepository _cardRepository;
        private readonly IGoalRepository _goalRepository;
        private readonly IPlayerRepository _playerRepository;

        public GameUpdateService(IGameRepository gameRepository,
            IAttachmentRepository attachmentRepository,
            ICardRepository cardRepository,
            IGoalRepository goalRepository,
            IPlayerRepository playerRepository)
        { 
            _gameRepository = gameRepository;
            _attachmentRepository = attachmentRepository;
            _cardRepository = cardRepository;
            _goalRepository = goalRepository;
            _playerRepository = playerRepository;

        }

        public async Task<ActionResult<GameAttachmentDto>> CreateAttachment(GameAttachmentDto attachmentDto)
        {
            GameAttachment gameAttachment = ConversionUtil.GetModelFromDto(attachmentDto) as GameAttachment;
            await _attachmentRepository.Insert(gameAttachment);
            return ConversionUtil.GetDtoFromModel(gameAttachment) as GameAttachmentDto;

        }

        public async Task<ActionResult<CardDto>> CreateCard(CardDto cardDto)
        {
            Card card = ConversionUtil.GetModelFromDto(cardDto) as Card;
            await _cardRepository.Insert(card);
            return ConversionUtil.GetDtoFromModel(card) as CardDto;
        }


        public async Task<ActionResult<GoalDto>> CreateGoal(GoalDto goalDto)
        {
            Goal goal = ConversionUtil.GetModelFromDto(goalDto) as Goal;
            await _goalRepository.Insert(goal);
            return ConversionUtil.GetDtoFromModel(goal) as GoalDto;
        }


        public async Task<ActionResult<GameDto>> UpdateGame(GameDto gameData)
        {
            Game gameModel = ConversionUtil.GetModelFromDto(gameData) as Game;
            await _gameRepository.UpdateGame(gameModel);
            return ConversionUtil.GetDtoFromModel(gameModel) as GameDto;
        }

    }
}
