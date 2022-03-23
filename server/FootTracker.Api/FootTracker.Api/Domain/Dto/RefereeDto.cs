namespace FootTracker.Api.Domain.Dto
{
    public class RefereeDto : IdentifiedDto
    {
        /// <summary>
        /// Nom complet sous format Prenom Nom.
        /// </summary>
        public string Name { get; set; }
    }
}
