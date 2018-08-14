	CREATE TABLE IF NOT EXISTS location(location_id uuid NOT NULL,
			country varchar NOT NULL,
			city varchar NOT NULL,
			created_at timestamp with time zone NOT NULL,
			modified_at timestamp with time zone NOT NULL,
			CONSTRAINT pk_location PRIMARY KEY (location_id)
			);
			CREATE TABLE IF NOT EXISTS time (
			time_id uuid NOT NULL,
			month int NOT NULL,
			quarter int NOT NULL,
			year int NOT NULL,
			created_at timestamp with time zone NOT NULL,
			modified_at timestamp with time zone NOT NULL,
			PRIMARY KEY (time_id)
			);

			CREATE TABLE IF NOT EXISTS product (
			product_id uuid NOT NULL,
			item int NOT NULL,
			class varchar NOT NULL,
			inventory varchar NOT NULL,
			created_at timestamp with time zone NOT NULL,
			modified_at timestamp with time zone NOT NULL,
			PRIMARY KEY (product_id)
			);

			CREATE TABLE IF NOT EXISTS sales (
			product_id uuid NOT NULL,
			time_id uuid NOT NULL,
			location_id uuid NOT NULL,
			dollars money NOT NULL,
			created_at timestamp with time zone NOT NULL,
			modified_at timestamp with time zone NOT NULL,
			PRIMARY KEY (product_id,time_id,location_id),CONSTRAINT fk_location FOREIGN
			KEY (location_id) REFERENCES location(location_id),
			CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES
			product(product_id),CONSTRAINT fk_time FOREIGN KEY (time_id)
			REFERENCES time(time_id)
			);