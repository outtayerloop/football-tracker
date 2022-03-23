using FootTracker.Api.Utils;
using System;
using System.Collections.Generic;

namespace FootTracker.Api.Domain.Models
{
    public class Game : BaseModel
    {
        /// <summary>
        /// Date et heure du match.
        /// </summary>
        public DateTime DateTime { get; set; }

        /// <summary>
        /// Adresse complete du match issue de la geolocalisation Google Maps.
        /// </summary>
        public string Address { get; set; }

        /// <summary>
        /// Determine si le match a ete valide ou non (apres modification terminee).
        /// Par defaut false.
        /// </summary>
        public bool IsValidated { get; set; } = false;

        public int TrackedTeamScore { get; set; }

        public int OpponentScore { get; set; }

        /// <summary>
        /// Nombre de sorties.
        /// </summary>
        public int OutCount { get; set; }

        public int PenaltyCount { get; set; }

        /// <summary>
        /// Nombre de coup-francs.
        /// </summary>
        public int FreeKickCount { get; set; }

        public int CornerCount { get; set; }

        public int OccasionCount { get; set; }

        public int FaultCount { get; set; }

        public int StopCount { get; set; }

        /// <summary>
        /// Nombre de hors-jeux.
        /// </summary>
        public int OffsideCount { get; set; }

        /// <summary>
        /// Duree des prolongations du match. 
        /// 0 seconde par defaut.
        /// </summary>
        public TimeSpan OverTime { get; set; }

        /// <summary>
        /// Nombre total de possessions. 
        /// Pour chaque joueur de l'equipe suivie, ce compteur est incremente de 1.
        /// </summary>
        public int TrackedTeamPossessionCount { get; set; }

        /// <summary>
        /// Nombre total de possessions. 
        /// Pour chaque joueur de l'equipe adverse, ce compteur est incremente de 1.
        /// </summary>
        public int OpponentTeamPossessionCount { get; set; }

        public int TrackedTeamId { get; set; }

        public Team TrackedTeam { get; set; }

        public int OpponentTeamId { get; set; }

        public Team OpponentTeam { get; set; }

        public int RefereeId { get; set; }

        public Referee Referee { get; set; }

        public int ChampionshipId { get; set; }

        public Championship Championship { get; set; }

        public ICollection<GameAttachment> Attachments { get; set; }

        public ICollection<Card> Cards { get; set; }

        public ICollection<Goal> Goals { get; set; }

        public static TimeSpan GetOverTimeFromText(string overtime)
        {
            return !StringUtil.IsNullOrEmptyOrWhiteSpaceInput(overtime)
                ? TimeSpan.Parse(overtime)
                : TimeSpan.FromSeconds(0);
        }
    }
}
