using FootTracker.Api.Database.Configuration;
using FootTracker.Api.Domain.Models;
using Microsoft.EntityFrameworkCore;

namespace FootTracker.Api.Database
{
    public class FootTrackerDbContext : DbContext
    {
        public FootTrackerDbContext(DbContextOptions<FootTrackerDbContext> options) : base(options) { }

        public DbSet<Championship> Championships { get; set; }

        public DbSet<Referee> Referees { get; set; }

        public DbSet<Team> Teams { get; set; }

        public DbSet<Player> Players { get; set; }

        public DbSet<Membership> Memberships { get; set; }

        public DbSet<Game> Games { get; set; }

        public DbSet<GameAttachment> Attachments { get; set; }

        public DbSet<Card> Cards { get; set; }

        public DbSet<Goal> Goals { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // Configuration des competitions.
            var championshipConfiguration = new ChampionshipConfiguration(modelBuilder);
            championshipConfiguration.Configure();

            // Configuration des arbitres.
            var refereeConfiguration = new RefereeConfiguration(modelBuilder);
            refereeConfiguration.Configure();

            // Configuration des equipes.
            var teamConfiguration = new TeamConfiguration(modelBuilder);
            teamConfiguration.Configure();

            // Configuration des joueurs.
            var playerConfiguration = new PlayerConfiguration(modelBuilder);
            playerConfiguration.Configure();

            // Configuration des associations joueur(s)/equipe(s).
            var membershipConfiguration = new MembershipConfiguration(modelBuilder);
            membershipConfiguration.Configure();

            // Configuration des match.
            var gameConfiguration = new GameConfiguration(modelBuilder);
            gameConfiguration.Configure();

            // Configuration des photos.
            var attachmentConfiguration = new AttachmentConfiguration(modelBuilder);
            attachmentConfiguration.Configure();

            // Configuration des cartons jaunes/rouges.
            var cardConfiguration = new CardConfiguration(modelBuilder);
            cardConfiguration.Configure();

            // Configuration des buts marques.
            var goalConfiguration = new GoalConfiguration(modelBuilder);
            goalConfiguration.Configure();
        }

    }
}
