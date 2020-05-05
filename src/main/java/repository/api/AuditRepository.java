package repository.api;

import model.entity.Audit;

public interface AuditRepository extends Crud<Audit> {
    Audit findByUsername(String username);
}
