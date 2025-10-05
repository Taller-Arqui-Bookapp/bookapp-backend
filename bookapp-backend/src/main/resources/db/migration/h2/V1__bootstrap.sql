-- Esquema base (H2 compatible)
create table if not exists users (
  id           uuid primary key,
  name         varchar(255) not null,
  email        varchar(255) not null unique,
  created_at   timestamp    not null
);

create table if not exists books (
  id             uuid primary key,
  title          varchar(255) not null,
  author         varchar(255) not null,
  isbn           varchar(64)  not null unique,
  published_year int,
  created_at     timestamp    not null
);

create table if not exists user_books (
  user_id    uuid not null,
  book_id    uuid not null,
  assigned_at timestamp not null,
  primary key (user_id, book_id),
  constraint fk_user_books_user foreign key (user_id) references users(id) on delete cascade,
  constraint fk_user_books_book foreign key (book_id) references books(id) on delete cascade
);

create index if not exists idx_user_books_user on user_books(user_id);
create index if not exists idx_user_books_book on user_books(book_id);
