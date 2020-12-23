CREATE SEQUENCE public.certificate_alias_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.certificate_alias (
    id bigint NOT NULL,
    calias character varying(150) NOT NULL,
    apassword character varying(100) NOT NULL,
    id_user bigint NOT NULL
);

CREATE TABLE public.token_user (
     id bigint NOT NULL,
     token_id character varying(100) NOT NULL,
     user_id bigint NOT NULL
);

CREATE SEQUENCE public.token_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE public.user_role (
    id bigint NOT NULL,
    role character varying(128) NOT NULL
);

CREATE SEQUENCE public.user_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.users (
    id bigint NOT NULL,
    login character varying(100) NOT NULL,
    upassword character varying(100),
    enabled numeric(1,0) DEFAULT 1 NOT NULL,
    id_user_role bigint DEFAULT 2
);

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.certificate_alias ALTER COLUMN id SET DEFAULT nextval('public.certificate_alias_id_seq');
ALTER TABLE public.token_user ALTER COLUMN id SET DEFAULT nextval('public.token_user_id_seq');
ALTER TABLE public.user_role ALTER COLUMN id SET DEFAULT nextval('public.user_role_id_seq');
ALTER TABLE public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq');


ALTER TABLE public.certificate_alias ADD CONSTRAINT certificate_alias_pk PRIMARY KEY (id);
ALTER TABLE public.token_user ADD CONSTRAINT token_user_pk PRIMARY KEY (id);
ALTER TABLE public.user_role ADD CONSTRAINT user_role_pk PRIMARY KEY (id);
ALTER TABLE public.users ADD CONSTRAINT users_pk PRIMARY KEY (id);

alter table public.certificate_alias add constraint certificate_alias_calias_uindex unique (calias);
alter table public.certificate_alias add constraint certificate_alias_id_user_uindex unique (id_user);

alter table public.token_user add constraint token_user_id_uindex unique (id);
alter table public.token_user add constraint token_user_tokenserial_uindex unique (token_id);
alter table public.token_user add constraint token_user_userid_uindex unique (user_id);

alter table public.user_role add constraint user_role_id_uindex unique (id);
alter table public.user_role add constraint user_role_role_uindex unique (role);
alter table public.users add constraint users_id_uindex unique (id);
alter table public.users add constraint users_login_uindex unique (login);

ALTER TABLE public.users
    ADD CONSTRAINT users_user_role_id_fk FOREIGN KEY (id_user_role) REFERENCES public.user_role(id);