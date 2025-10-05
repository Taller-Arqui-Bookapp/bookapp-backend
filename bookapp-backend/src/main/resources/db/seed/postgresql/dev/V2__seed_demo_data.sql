-- Habilitar UUID aleatorio (en tu Postgres Docker funciona)
create extension if not exists pgcrypto;

-- Usuarios (IDs fijos para reusar en asignaciones)
insert into users (id, name, email, created_at) values
('11111111-1111-1111-1111-111111111111','Ana Torres','ana@example.com', now()),
('22222222-2222-2222-2222-222222222222','Bruno Díaz','bruno@example.com', now()),
('33333333-3333-3333-3333-333333333333','Carla Gómez','carla@example.com', now())
on conflict (email) do nothing;

-- Libros
insert into books (id, title, author, isbn, published_year, created_at) values
('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1','El Alquimista','Paulo Coelho','9780061122415',1988, now()),
('aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2','Cien años de soledad','G. G. Márquez','9780307474728',1967, now()),
('aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaa3','Clean Code','Robert C. Martin','9780132350884',2008, now()),
('aaaaaaa4-aaaa-aaaa-aaaa-aaaaaaaaaaa4','Kotlin in Action','Dmitry Jemerov','9781617293290',2017, now()),
('aaaaaaa5-aaaa-aaaa-aaaa-aaaaaaaaaaa5','Domain-Driven Design','Eric Evans','9780321125217',2003, now())
on conflict (isbn) do nothing;

-- Asignaciones (N:N)
insert into user_books (user_id, book_id, assigned_at) values
('11111111-1111-1111-1111-111111111111','aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaa3', now()),
('11111111-1111-1111-1111-111111111111','aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2', now()),
('22222222-2222-2222-2222-222222222222','aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', now()),
('33333333-3333-3333-3333-333333333333','aaaaaaa5-aaaa-aaaa-aaaa-aaaaaaaaaaa5', now())
on conflict do nothing;  -- evita error si ya existen
