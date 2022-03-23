using FootTracker.Api.Domain.Dto;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;

namespace FootTracker.Api.Domain.Models
{
    public class Team : BaseModel
    {
        public string Name { get; set; }

        public ICollection<Membership> Memberships { get; set; }

        [NotMapped]
        public List<Player> Players { get; set; }

        public ICollection<Game> TrackedTeamGames { get; set; }

        public ICollection<Game> OpponentTeamGames { get; set; }

        public void SetTeamPlayers()
            => Players = Memberships.Select(membership => membership.Player).ToList();

        public void SetCommonMembersFromDto(BaseTeamDto teamToConvert)
        {
            Id = teamToConvert.Id;
            Name = teamToConvert.Name;
        }

        public bool HasPlayers()
            => Players != null && Players.Count > 0;
    }
}
