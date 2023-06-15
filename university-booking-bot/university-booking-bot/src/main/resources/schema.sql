create table Booking
(
    id bigint not null,
    room_id bigint not null,
    startTIme bigint not null,
    endTime bigint not null,
--   name varchar(255) not null,
--   author varchar(255) not null,
    primary key(id)
);