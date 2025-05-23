--
-- PostgreSQL database dump
--

-- Dumped from database version 17.0
-- Dumped by pg_dump version 17.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: ordersdb; Type: DATABASE; Schema: -; Owner: root
--

CREATE DATABASE ordersdb WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';


ALTER DATABASE ordersdb OWNER TO root;

\connect ordersdb

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: prevent_update_clients_id(); Type: FUNCTION; Schema: public; Owner: root
--

CREATE FUNCTION public.prevent_update_clients_id() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.id IS DISTINCT FROM OLD.id THEN
        RAISE EXCEPTION 'id is immutable';
    END IF;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.prevent_update_clients_id() OWNER TO root;

--
-- Name: prevent_update_log_amount(); Type: FUNCTION; Schema: public; Owner: root
--

CREATE FUNCTION public.prevent_update_log_amount() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.amount IS DISTINCT FROM OLD.amount THEN
        RAISE EXCEPTION 'amount is immutable';
    END IF;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.prevent_update_log_amount() OWNER TO root;

--
-- Name: prevent_update_log_id(); Type: FUNCTION; Schema: public; Owner: root
--

CREATE FUNCTION public.prevent_update_log_id() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.id IS DISTINCT FROM OLD.id THEN
        RAISE EXCEPTION 'id is immutable';
    END IF;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.prevent_update_log_id() OWNER TO root;

--
-- Name: prevent_update_log_orderid(); Type: FUNCTION; Schema: public; Owner: root
--

CREATE FUNCTION public.prevent_update_log_orderid() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.orderId IS DISTINCT FROM OLD.orderId THEN
        RAISE EXCEPTION 'orderId is immutable';
    END IF;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.prevent_update_log_orderid() OWNER TO root;

--
-- Name: prevent_update_log_timestamp(); Type: FUNCTION; Schema: public; Owner: root
--

CREATE FUNCTION public.prevent_update_log_timestamp() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.timestamp IS DISTINCT FROM OLD.timestamp THEN
        RAISE EXCEPTION 'timestamp is immutable';
    END IF;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.prevent_update_log_timestamp() OWNER TO root;

--
-- Name: prevent_update_orders_id(); Type: FUNCTION; Schema: public; Owner: root
--

CREATE FUNCTION public.prevent_update_orders_id() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.id IS DISTINCT FROM OLD.id THEN
        RAISE EXCEPTION 'id is immutable';
    END IF;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.prevent_update_orders_id() OWNER TO root;

--
-- Name: prevent_update_products_id(); Type: FUNCTION; Schema: public; Owner: root
--

CREATE FUNCTION public.prevent_update_products_id() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.id IS DISTINCT FROM OLD.id THEN
        RAISE EXCEPTION 'id is immutable';
    END IF;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.prevent_update_products_id() OWNER TO root;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: clients; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.clients (
    id integer NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    email character varying(150) NOT NULL,
    address character varying(300) NOT NULL
);


ALTER TABLE public.clients OWNER TO root;

--
-- Name: clients_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

ALTER TABLE public.clients ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.clients_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: log; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.log (
    id integer NOT NULL,
    orderid integer NOT NULL,
    amount double precision NOT NULL,
    "timestamp" timestamp without time zone NOT NULL
);


ALTER TABLE public.log OWNER TO root;

--
-- Name: log_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

ALTER TABLE public.log ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: orders; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.orders (
    id integer NOT NULL,
    client_id integer NOT NULL,
    product_id integer NOT NULL,
    quantity integer NOT NULL,
    total_price double precision NOT NULL,
    order_date timestamp without time zone NOT NULL
);


ALTER TABLE public.orders OWNER TO root;

--
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

ALTER TABLE public.orders ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: products; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.products (
    id integer NOT NULL,
    name character varying(300) NOT NULL,
    price double precision NOT NULL,
    stock integer NOT NULL
);


ALTER TABLE public.products OWNER TO root;

--
-- Name: products_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

ALTER TABLE public.products ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Data for Name: clients; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.clients (id, first_name, last_name, email, address) FROM stdin;
2	Mathew	Comma	mathew_comma@icloud.com	301 Della St, Elizabethtown, North Carolina, 28337, USA
3	Lucas	Jones	joneslucas@gmail.com	924 51st Hwy, Madison, Mississippi, 39110, USA
4	Mariana	Joy	mary_joy@gmail.com	757 Inverness Dr, West Chester, Pennsylvania, 19380, USA
5	Johanna	Zieflig	joha@icloud.com	Im Ziegelfeld 1, 7 OG, 54815, Germany
1	Julia	Andrews	julia.andrews@yahoo.com	1000 Vicars Way Lndg, Ponte Vedra Beach, Florida, 32082, USA
6	John	Doe	john.doe@gmail.com	123 Main St, Springfield
7	Jane	Smith	jane.smith@yahoo.com	456 Elm St, Springfield
8	Robert	Johnson	robert.johnson@outlook.com	789 Oak St, Springfield
9	Emily	Brown	emily.brown@icloud.com	101 Pine St, Springfield
10	Michael	Williams	michael.williams@aol.com	202 Birch St, Springfield
11	Sarah	Jones	sarah.jones@gmail.com	303 Cedar St, Springfield
12	David	Garcia	david.garcia@yahoo.com	404 Maple St, Springfield
13	Laura	Martinez	laura.martinez@outlook.com	505 Walnut St, Springfield
14	Daniel	Lopez	daniel.lopez@icloud.com	606 Cherry St, Springfield
15	Sophia	Gonzalez	sophia.gonzalez@aol.com	707 Chestnut St, Springfield
16	James	Harris	james.harris@gmail.com	808 Willow St, Springfield
17	Olivia	Clark	olivia.clark@yahoo.com	909 Aspen St, Springfield
18	William	Lewis	william.lewis@outlook.com	1010 Poplar St, Springfield
19	Emma	Young	emma.young@icloud.com	1111 Magnolia St, Springfield
20	Benjamin	Hall	benjamin.hall@aol.com	1212 Fir St, Springfield
21	Charlotte	Allen	charlotte.allen@gmail.com	1313 Redwood St, Springfield
23	Mia	Adams	mia.adams@outlook.com	1515 Cypress St, Springfield
24	Ethan	Nelson	ethan.nelson@icloud.com	1616 Palm St, Springfield
25	Isabella	Baker	isabella.baker@aol.com	1717 Sycamore St, Springfield
22	Alexander	Scott	alexander.scott@yahoo.com	1414 Spruce St, Springfield, USA
\.


--
-- Data for Name: log; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.log (id, orderid, amount, "timestamp") FROM stdin;
1	1	399.6	2025-05-12 17:32:28.096408
2	2	59.99	2025-05-14 16:11:05.870183
3	3	1299.99	2025-05-17 11:17:03.331289
4	4	119.98	2025-05-17 11:17:13.220039
5	5	109.99	2025-05-17 11:19:06.893885
6	6	749.97	2025-05-17 11:35:42.000701
7	7	549.99	2025-05-17 11:35:57.683315
8	8	259.98	2025-05-17 11:36:12.978864
9	9	199.98	2025-05-17 15:08:00.644869
10	10	99.99	2025-05-17 15:08:12.1047
11	10	99.99	2025-05-17 15:08:12.1047
\.


--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.orders (id, client_id, product_id, quantity, total_price, order_date) FROM stdin;
1	1	4	2	399.6	2025-05-12 17:32:28.096408
2	2	3	1	59.99	2025-05-14 16:11:05.870183
3	8	6	1	1299.99	2025-05-17 11:17:03.331289
4	9	9	2	119.98	2025-05-17 11:17:13.220039
5	24	10	1	109.99	2025-05-17 11:19:06.893885
6	3	13	3	749.97	2025-05-17 11:35:42.000701
7	9	17	1	549.99	2025-05-17 11:35:57.683315
8	19	25	2	259.98	2025-05-17 11:36:12.978864
10	18	26	1	99.99	2025-05-17 15:08:12.1047
\.


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.products (id, name, price, stock) FROM stdin;
2	Mechanical Keyboard	79.99	99
5	27-inch Monitor	1999.99	9
1	Wireless Mouse	29.99	147
3	Gaming Headset	59.99	73
7	Smartphone X	899.99	100
8	Wireless Earbuds	149.99	200
11	Ultra HD Monitor	349.99	70
12	Portable SSD 1TB	129.99	120
14	Bluetooth Speaker	79.99	140
15	External Hard Drive 2TB	99.99	110
16	Noise Cancelling Headphones	199.99	60
18	Fitness Tracker	59.99	160
19	4K Streaming Device	39.99	130
21	Graphic Tablet	199.99	40
22	USB-C Docking Station	69.99	95
23	VR Headset	399.99	30
24	Smart Home Hub	149.99	55
20	Wireless Router AX3000	89.99	83
6	Laptop Pro 15"	1299.99	49
9	Gaming Mouse	59.99	148
13	Smartwatch Series 7	249.99	87
17	Tablet Air 11"	549.99	74
25	E-Reader Paperwhite	129.99	123
10	Mechanical Keyboard X Lights	109.99	79
4	USB-C Docking Station	199.8	48
27	Smartphone Case 	52.38	2
26	Noise Cancelling Earbuds	99.99	0
\.


--
-- Name: clients_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.clients_id_seq', 26, true);


--
-- Name: log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.log_id_seq', 11, true);


--
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.orders_id_seq', 10, true);


--
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.products_id_seq', 27, true);


--
-- Name: clients clients_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT clients_pkey PRIMARY KEY (id);


--
-- Name: log log_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.log
    ADD CONSTRAINT log_pkey PRIMARY KEY (id);


--
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- Name: clients no_update_clients_id; Type: TRIGGER; Schema: public; Owner: root
--

CREATE TRIGGER no_update_clients_id BEFORE UPDATE ON public.clients FOR EACH ROW EXECUTE FUNCTION public.prevent_update_clients_id();


--
-- Name: log no_update_log_amount; Type: TRIGGER; Schema: public; Owner: root
--

CREATE TRIGGER no_update_log_amount BEFORE UPDATE ON public.log FOR EACH ROW EXECUTE FUNCTION public.prevent_update_log_amount();


--
-- Name: log no_update_log_id; Type: TRIGGER; Schema: public; Owner: root
--

CREATE TRIGGER no_update_log_id BEFORE UPDATE ON public.log FOR EACH ROW EXECUTE FUNCTION public.prevent_update_log_id();


--
-- Name: log no_update_log_orderid; Type: TRIGGER; Schema: public; Owner: root
--

CREATE TRIGGER no_update_log_orderid BEFORE UPDATE ON public.log FOR EACH ROW EXECUTE FUNCTION public.prevent_update_log_orderid();


--
-- Name: log no_update_log_timestamp; Type: TRIGGER; Schema: public; Owner: root
--

CREATE TRIGGER no_update_log_timestamp BEFORE UPDATE ON public.log FOR EACH ROW EXECUTE FUNCTION public.prevent_update_log_timestamp();


--
-- Name: orders no_update_orders_id; Type: TRIGGER; Schema: public; Owner: root
--

CREATE TRIGGER no_update_orders_id BEFORE UPDATE ON public.orders FOR EACH ROW EXECUTE FUNCTION public.prevent_update_orders_id();


--
-- Name: products no_update_products_id; Type: TRIGGER; Schema: public; Owner: root
--

CREATE TRIGGER no_update_products_id BEFORE UPDATE ON public.products FOR EACH ROW EXECUTE FUNCTION public.prevent_update_products_id();


--
-- Name: orders orders_client_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_client_id_fkey FOREIGN KEY (client_id) REFERENCES public.clients(id);


--
-- Name: orders orders_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- PostgreSQL database dump complete
--

