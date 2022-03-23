using FootTracker.Api.Database;
using FootTracker.Api.Domain.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FootTracker.Api.Repositories
{
    public class FootTrackerRepository<T> : IFootTrackerRepository<T> where T : BaseModel
    {
        protected readonly FootTrackerDbContext _dbContext;

        protected readonly DbSet<T> _models;

        public FootTrackerRepository(FootTrackerDbContext dbContext)
        {
            _dbContext = dbContext;
            _models = dbContext.Set<T>();
        }

        public virtual List<T> GetAll()
            => _models.ToList();

        public T GetById(int id)
            => _models.FirstOrDefault(model => model.Id == id);

        public async Task<T> Insert(T model)
        {
            if (model == null)
                throw new ArgumentNullException("Can not insert null model");
            _models.Add(model);
            await _dbContext.SaveChangesAsync();
            return model;
        }

        public async Task<T> UpdateGame(T model)
        {
            if (model == null)
                throw new ArgumentNullException("Can not update model with null data");
            _models.Update(model);
            await _dbContext.SaveChangesAsync();
            return model;
        }

        public async Task Delete(int id)
        {
            T modelToDelete = GetById(id);
            if (modelToDelete != null)
                _models.Remove(modelToDelete);
            await _dbContext.SaveChangesAsync();
        }      
    }
}
