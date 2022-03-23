using FootTracker.Api.Domain.Models;
using Microsoft.EntityFrameworkCore;

namespace FootTracker.Api.Database.Configuration
{
    public class RefereeConfiguration : BaseConfiguration
    {

        public RefereeConfiguration(ModelBuilder modelBuilder) : base(modelBuilder) { }

        public override void Configure()
        {
            _modelBuilder.Entity<Referee>()
                .ToTable("REFEREES")
                .HasKey(referee => referee.Id)
                .HasName("PK_Referee_Id");

            _modelBuilder.Entity<Referee>()
                .Property(referee => referee.Id)
                .HasColumnName("referee_id")
                .HasColumnType("INT")
                .IsRequired(true);

            _modelBuilder.Entity<Referee>()
                .Property(referee => referee.FullName)
                .HasColumnName("referee_full_name")
                .HasColumnType("VARCHAR(300)")
                .IsRequired(true);

            _modelBuilder.Entity<Referee>()
                .HasIndex(referee => referee.FullName)
                .IsUnique()
                .HasDatabaseName("UC_Referee_Full_Name");

            _modelBuilder.Entity<Referee>()
                .HasMany(referee => referee.Games)
                .WithOne(game => game.Referee)
                .HasForeignKey(game => game.RefereeId)
                .HasConstraintName("FK_Game_Referee_Id")
                .OnDelete(DeleteBehavior.Cascade);
        }
    }
}
