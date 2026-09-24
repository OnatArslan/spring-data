-- Kimlik: anlamsız, değişmez uuid (v7, application tarafında üretilir).
-- Business tekillik ayrı UNIQUE constraint olarak ifade edilir.
CREATE TABLE projects
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(120) NOT NULL,
    status     VARCHAR(32)  NOT NULL,
    version    BIGINT       NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ  NOT NULL,
    updated_at TIMESTAMPTZ  NOT NULL,

    -- Constraint adları bilinçli: exception'dan domain hatasına çeviri bu adla yapılır
    CONSTRAINT uq_projects_name UNIQUE (name),
    CONSTRAINT ck_projects_status
        CHECK (status IN ('ACTIVE', 'ARCHIVED'))
);

-- 1:1 ilişki: FK üzerindeki UNIQUE, "bir project'in en fazla bir settings'i" kuralını DB'de tutar
CREATE TABLE project_settings
(
    id                  UUID PRIMARY KEY,
    project_id          UUID        NOT NULL,
    default_todo_status VARCHAR(32) NOT NULL,
    max_open_todos      INTEGER     NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL,
    updated_at          TIMESTAMPTZ NOT NULL,

    CONSTRAINT uq_project_settings_project UNIQUE (project_id),
    CONSTRAINT fk_project_settings_project
        FOREIGN KEY (project_id) REFERENCES projects (id),

    CONSTRAINT ck_project_settings_max_open_todos
        CHECK (max_open_todos > 0)
);

CREATE TABLE todos
(
    id          UUID PRIMARY KEY,
    project_id  UUID         NOT NULL,
    title       VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    status      VARCHAR(32)  NOT NULL,
    priority    SMALLINT     NOT NULL,
    due_date    DATE,
    version     BIGINT       NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ  NOT NULL,
    updated_at  TIMESTAMPTZ  NOT NULL,

    -- ON DELETE yok = NO ACTION: silme politikası uygulamada açıkça verilir (§9)
    CONSTRAINT fk_todos_project
        FOREIGN KEY (project_id) REFERENCES projects (id),

    CONSTRAINT ck_todos_status
        CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE')),

    -- CHECK NULL'da geçer; NOT NULL bu yüzden ayrı yazılır
    CONSTRAINT ck_todos_priority
        CHECK (priority BETWEEN 1 AND 5)
);

CREATE TABLE tags
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(80) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT uq_tags_name UNIQUE (name)
);

-- Association kendi satırı olan bir kavram: payload (added_at) ve kendi kimliği var (§8)
CREATE TABLE todo_tags
(
    id       UUID PRIMARY KEY,
    todo_id  UUID        NOT NULL,
    tag_id   UUID        NOT NULL,
    added_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT uq_todo_tags_todo_tag UNIQUE (todo_id, tag_id),

    CONSTRAINT fk_todo_tags_todo
        FOREIGN KEY (todo_id) REFERENCES todos (id),

    CONSTRAINT fk_todo_tags_tag
        FOREIGN KEY (tag_id) REFERENCES tags (id)
);

-- PostgreSQL PK ve UNIQUE için index'i kendisi oluşturur, FK kolonları için oluşturmaz
CREATE INDEX ix_todos_project_id
    ON todos (project_id);

CREATE INDEX ix_todos_project_status
    ON todos (project_id, status);

CREATE INDEX ix_todo_tags_todo_id
    ON todo_tags (todo_id);

CREATE INDEX ix_todo_tags_tag_id
    ON todo_tags (tag_id);