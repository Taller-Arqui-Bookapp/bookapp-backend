-- Esquema base (PostgreSQL)
create table if not exists users (
  id           uuid primary key,
  name         text        not null,
  email        text        not null unique,
  created_at   timestamptz not null default now()
);

create table if not exists books (
  id             uuid primary key,
  title          text        not null,
  author         text        not null,
  isbn           text        not null unique,
  published_year int,
  created_at     timestamptz not null default now()
);

create table if not exists user_books (
  user_id    uuid not null references users(id) on delete cascade,
  book_id    uuid not null references books(id) on delete cascade,
  assigned_at timestamptz not null default now(),
  primary key (user_id, book_id)
);

-- Índices recomendados (FK)
create index if not exists idx_user_books_user on user_books(user_id);
create index if not exists idx_user_books_book on user_books(book_id);
