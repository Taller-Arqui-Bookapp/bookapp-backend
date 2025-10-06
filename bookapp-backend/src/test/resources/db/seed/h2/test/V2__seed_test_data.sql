-- USERS (UUIDs fijos para usarlos en asserts)
INSERT INTO users (id, name, email, created_at) VALUES
('11111111-1111-1111-1111-111111111111','Ana Torres','ana@example.com', CURRENT_TIMESTAMP),
('22222222-2222-2222-2222-222222222222','Bruno Díaz','bruno@example.com', CURRENT_TIMESTAMP),
('33333333-3333-3333-3333-333333333333','Carla Gómez','carla@example.com', CURRENT_TIMESTAMP);

-- BOOKS
INSERT INTO books (id, title, author, isbn, published_year, created_at) VALUES
('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1','El Alquimista','Paulo Coelho','9780061122415',1988, CURRENT_TIMESTAMP),
('aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2','Cien años de soledad','G. G. Márquez','9780307474728',1967, CURRENT_TIMESTAMP),
('aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaa3','Clean Code','Robert C. Martin','9780132350884',2008, CURRENT_TIMESTAMP),
('aaaaaaa4-aaaa-aaaa-aaaa-aaaaaaaaaaa4','Kotlin in Action','Dmitry Jemerov','9781617293290',2017, CURRENT_TIMESTAMP),
('aaaaaaa5-aaaa-aaaa-aaaa-aaaaaaaaaaa5','Domain-Driven Design','Eric Evans','9780321125217',2003, CURRENT_TIMESTAMP);

-- USER_BOOKS (N:N)
INSERT INTO user_books (user_id, book_id, assigned_at) VALUES
('11111111-1111-1111-1111-111111111111','aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaa3', CURRENT_TIMESTAMP),
('11111111-1111-1111-1111-111111111111','aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2', CURRENT_TIMESTAMP),
('22222222-2222-2222-2222-222222222222','aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', CURRENT_TIMESTAMP),
('33333333-3333-3333-3333-333333333333','aaaaaaa5-aaaa-aaaa-aaaa-aaaaaaaaaaa5', CURRENT_TIMESTAMP);
