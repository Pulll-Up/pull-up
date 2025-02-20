create table daily_quiz
(
    id              bigint auto_increment primary key,
    interview_id    bigint null,
    member_id       bigint null,
    question        varchar(255) null
);

create table interview
(
    created_at      datetime(6) not null,
    id              bigint auto_increment primary key,
    modified_at     datetime(6) not null,
    answer          varchar(255) not null,
    question        varchar(255) not null,
    subject         varchar(255) not null
);

create table interview_hint
(
    created_at      datetime(6) not null,
    id              bigint auto_increment primary key,
    interview_id    bigint not null,
    modified_at     datetime(6) not null,
    keyword         varchar(255) not null,
    constraint FK2obq3cjim9x1rb710grsva8va foreign key (interview_id) references interview (id)
);

create table member
(
    created_at            datetime(6) not null,
    id                    bigint auto_increment primary key,
    modified_at           datetime(6) not null,
    email                 varchar(255) not null,
    name                  varchar(255) not null,
    profile_image_url     varchar(255) not null,
    provider_id           varchar(255) not null,
    o_auth_provider       enum ('GOOGLE', 'KAKAO', 'NAVER') not null,
    role                  enum ('ADMIN', 'USER') not null,
    solved_days           bigint null,
    member_game_result_id bigint null,
    constraint UKr0450h0wb8fque584kov6wjxa unique (member_game_result_id)
);

create table device_token
(
    id              bigint auto_increment primary key,
    created_at      datetime(6) not null,
    modified_at     datetime(6) not null,
    token           varchar(255) not null,
    member_id       bigint not null,
    constraint FKqbpc6xf21ge7sek3op9t4ru3v foreign key (member_id) references member (id)
);

create table exam
(
    round            int not null,
    score            int not null,
    created_at       datetime(6) not null,
    id               bigint auto_increment primary key,
    member_id        bigint not null,
    modified_at      datetime(6) not null,
    difficulty_level enum ('EASY', 'HARD', 'MEDIUM') not null,
    constraint FK5gp0h4t9lbbwiyr4m0n13h1xc foreign key (member_id) references member (id)
);

create table interest_subject
(
    created_at      datetime(6) not null,
    id              bigint auto_increment primary key,
    member_id       bigint not null,
    modified_at     datetime(6) not null,
    subject         varchar(255) not null,
    constraint FKd59wx3td69yqo0bvkf5lpa1ei foreign key (member_id) references member (id)
);

create table interview_answer
(
    created_at      datetime(6) not null,
    id              bigint auto_increment primary key,
    interview_id    bigint not null,
    member_id       bigint not null,
    modified_at     datetime(6) not null,
    answer          varchar(255) not null,
    strength        varchar(255) null,
    weakness        varchar(255) null,
    constraint FKe9yi5v8qnnru9q0yb2o2napim foreign key (interview_id) references interview (id),
    constraint FKqd1500f55vj0jvghn7n6b8cnq foreign key (member_id) references member (id)
);

create table comment
(
    created_at          datetime(6) not null,
    id                  bigint auto_increment primary key,
    interview_answer_id bigint not null,
    member_id           bigint not null,
    modified_at         datetime(6) not null,
    content             varchar(255) not null,
    constraint FKmrrrpi513ssu63i2783jyiv9m foreign key (member_id) references member (id),
    constraint FKxajdxy57pxdobb36vmx99ynu foreign key (interview_answer_id) references interview_answer (id)
);

create table likes
(
    is_liked            bit not null,
    created_at          datetime(6) not null,
    id                  bigint auto_increment primary key,
    interview_answer_id bigint not null,
    member_id           bigint not null,
    modified_at         datetime(6) not null,
    constraint FKa4vkf1skcfu5r6o5gfb5jf295 foreign key (member_id) references member (id),
    constraint FKt0ou36t7ljwb0m8ppldcknsio foreign key (interview_answer_id) references interview_answer (id)
);

create table member_exam_statistic
(
    total_count   int not null,
    wrong_count   int not null,
    created_at    datetime(6) not null,
    id            bigint auto_increment primary key,
    member_id     bigint not null,
    modified_at   datetime(6) not null,
    subject       enum ('ALGORITHM', 'COMPUTER_ARCHITECTURE', 'DATABASE', 'DATA_STRUCTURE', 'NETWORK', 'OPERATING_SYSTEM') not null,
    constraint FK7fc8ltg3xv03540eop3pe97y8 foreign key (member_id) references member (id)
);

create table member_game_result
(
    win_count        int not null,
    created_at       datetime(6) not null,
    id               bigint auto_increment primary key,
    modified_at      datetime(6) not null,
    draw_count       int not null,
    lose_count       int not null,
    member_id        bigint not null,
    constraint FKbbox4putvbdhkc24bwnp7gh85 foreign key (member_id) references member (id)
);

alter table member
    add constraint FK1s4j7autfjv2tv90yh6bvjnkn foreign key (member_game_result_id) references member_game_result (id);

create table problem
(
    correct_rate     int not null,
    created_at       datetime(6) not null,
    id               bigint auto_increment primary key,
    modified_at      datetime(6) not null,
    answer           varchar(255) not null,
    explanation      text not null,
    question         varchar(255) not null,
    problem_type     enum ('MULTIPLE_CHOICE', 'SHORT_ANSWER') not null,
    subject          enum ('ALGORITHM', 'COMPUTER_ARCHITECTURE', 'DATABASE', 'DATA_STRUCTURE', 'NETWORK', 'OPERATING_SYSTEM') not null,
    total_count      int default 0 not null,
    correct_count    int default 0 not null
);

create table bookmark
(
    is_bookmarked     bit not null,
    created_at        datetime(6) not null,
    id                 bigint auto_increment primary key,
    member_id          bigint not null,
    modified_at        datetime(6) not null,
    problem_id         bigint not null,
    constraint FK2p0jv1kw42c9x87wsi1tb2gbf foreign key (problem_id) references problem (id),
    constraint FK5bm7rup91j277mc7gg63akie2 foreign key (member_id) references member (id)
);

create table exam_problem
(
    answer_status          bit not null,
    created_at            datetime(6) not null,
    exam_id               bigint not null,
    id                    bigint auto_increment primary key,
    modified_at           datetime(6) not null,
    problem_id            bigint not null,
    member_checked_answer varchar(255) not null,
    constraint FK99c7gamw5uq0w67kandc0ltg foreign key (problem_id) references problem (id),
    constraint FKf2jdg2plyq9sm84qk21r8gci foreign key (exam_id) references exam (id)
);

create table problem_answer
(
    id         bigint auto_increment primary key,
    answer     varchar(255) not null,
    problem_id bigint not null,
    constraint FKr2arekcqis0g8jt8g1qcclc3h foreign key (problem_id) references problem (id)
);

create table problem_option
(
    created_at    datetime(6) not null,
    id            bigint auto_increment primary key,
    modified_at   datetime(6) not null,
    problem_id    bigint not null,
    content       varchar(255) not null,
    constraint FK4gi6sb33qpgdbmiq12giv560r foreign key (problem_id) references problem (id) on delete cascade
);

