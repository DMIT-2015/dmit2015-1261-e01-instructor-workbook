package dmit2015.service;

import dmit2015.model.Expense;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Named("jakartaPersistenceExpenseService")
@ApplicationScoped
public class ExpenseJpaService implements ExpenseService {

    // Assign a unitName if there are more than one persistence unit defined in persistence.xml
    @PersistenceContext //(unitName="pu-name-in-persistence.xml")
    private EntityManager entityManager;

    @Override
    @Transactional
    public Expense createExpense(Expense expense) {
        // If the primary key is not an identity column then write code below here to
        // 1) Generate a new primary key value
        // 2) Set the primary key value for the new entity

        entityManager.persist(expense);
        return expense;
    }

    @Override
    public Optional<Expense> getExpenseById(Long id) {
        try {
            Expense querySingleResult = entityManager.find(Expense.class, id);
            if (querySingleResult != null) {
                return Optional.of(querySingleResult);
            }
        } catch (Exception ex) {
            // id value not found
            throw new RuntimeException(ex);
        }
        return Optional.empty();
    }

    @Override
    public List<Expense> getAllExpenses() {
        return entityManager.createQuery("SELECT o FROM Expense o ", Expense.class)
                .getResultList();
    }

    @Override
    @Transactional
    public Expense updateExpense(Expense expense) {

        Optional<Expense> optionalExpense = getExpenseById(expense.getId());
        if (optionalExpense.isEmpty()) {
            String errorMessage = String.format("The id %s does not exists in the system.", expense.getId());
            throw new RuntimeException(errorMessage);
        } else {
            var existingExpense = optionalExpense.orElseThrow();
            // Update only properties that is editable by the end user
            // TODO: Copy each edit property from expense to existingExpense
            // existingExpense.setPropertyName(expense.getPropertyName());

            expense = entityManager.merge(existingExpense);
        }
        return expense;
    }

    @Override
    @Transactional
    public void deleteExpenseById(Long id) {
        Optional<Expense> optionalExpense = getExpenseById(id);
        if (optionalExpense.isPresent()) {
            Expense expense = optionalExpense.orElseThrow();
            // Write code to throw a RuntimeException if this entity contains child records
            entityManager.remove(expense);
        } else {
            throw new RuntimeException("Could not find Expense with id: " + id);
        }
    }

    @Override
    public long count() {
        return (long) entityManager.createQuery(
                "select count(e) from Expense e")
                .getSingleResult();
    }

    @Override
    public void deleteAllExpenses() {
        entityManager.createQuery("delete from Expense e").executeUpdate();
    }

}