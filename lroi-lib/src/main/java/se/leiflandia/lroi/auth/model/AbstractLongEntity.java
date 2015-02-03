package se.leiflandia.lroi.auth.model;

public abstract class AbstractLongEntity extends AbstractCreatedUpdatedEntity {
    private Long id;

    private AbstractLongEntity() { }

    public AbstractLongEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
