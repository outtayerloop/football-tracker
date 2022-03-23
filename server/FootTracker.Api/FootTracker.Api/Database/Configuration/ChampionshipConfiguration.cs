using FootTracker.Api.Domain.Models;
using Microsoft.EntityFrameworkCore;

namespace FootTracker.Api.Database.Configuration
{
    public class ChampionshipConfiguration : BaseConfiguration
    {
        public ChampionshipConfiguration(ModelBuilder modelBuilder) : base(modelBuilder) { }

        public override void Configure()
        {
            _modelBuilder.Entity<Championship>()
                .ToTable("CHAMPIONSHIPS")
                .HasKey(championship => championship.Id)
                .HasName("PK_Championship_Id");

            _modelBuilder.Entity<Championship>()
                .Property(championship => championship.Id)
                .HasColumnName("championship_id")
                .HasColumnType("INT")
                .IsRequired(true);

            _modelBuilder.Entity<Championship>()
                .Property(championship => championship.Name)
                .HasColumnName("championship_name")
                .HasColumnType("VARCHAR(100)")
                .IsRequired(true);

            _modelBuilder.Entity<Championship>()
                .HasIndex(championship => championship.Name)
                .IsUnique()
                .HasDatabaseName("UC_Championship_Name");

            _modelBuilder.Entity<Championship>()
                .HasMany(championship => championship.Games)
                .WithOne(game => game.Championship)
                .HasForeignKey(game => game.ChampionshipId)
                .HasConstraintName("FK_Game_Championship_Id")
                .OnDelete(DeleteBehavior.Cascade);
        }
    }
}
