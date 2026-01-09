CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS franchises (
                            id UUID PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS branches (
                          id UUID PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          franchise_id UUID NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          CONSTRAINT fk_branch_franchise
                              FOREIGN KEY (franchise_id)
                                  REFERENCES franchises(id)
                                  ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS products (
                          id UUID PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          stock INT NOT NULL,
                          branch_id UUID NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          CONSTRAINT fk_product_branch
                              FOREIGN KEY (branch_id)
                                  REFERENCES branches(id)
                                  ON DELETE CASCADE
);