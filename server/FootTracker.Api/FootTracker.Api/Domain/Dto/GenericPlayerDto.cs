using FootTracker.Api.Domain.Models;
using System;

namespace FootTracker.Api.Domain.Dto
{
    public class GenericPlayerDto : IdentifiedDto
    {
        /// <summary>
        /// Nom complet sous format Prenom Nom.
        /// </summary>
        public string Name { get; set; }

        public string Position { get; set; }

        public void SetCommonMembersFromModel(Player playerToConvert)
        {
            Id = playerToConvert.Id;
            Name = playerToConvert.FullName;
            Position = Enum.GetName(typeof(PlayerPosition), playerToConvert.Position);
        }

    }
}
