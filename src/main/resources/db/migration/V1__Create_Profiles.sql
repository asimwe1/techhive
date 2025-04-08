CREATE TABLE user_profile (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              bio TEXT,
                              location VARCHAR(255),
                              website VARCHAR(255),
                              avatar VARCHAR(255),
                              FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE freelancer_profile (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    user_profile_id BIGINT NOT NULL,
                                    completed_jobs INT DEFAULT 0,
                                    rating DOUBLE DEFAULT 0.0,
                                    hourly_rate DOUBLE DEFAULT 0.0,
                                    FOREIGN KEY (user_profile_id) REFERENCES user_profile(id)
);

CREATE TABLE freelancer_profile_skills (
                                           freelancer_profile_id BIGINT NOT NULL,
                                           skills VARCHAR(255),
                                           FOREIGN KEY (freelancer_profile_id) REFERENCES freelancer_profile(id)
);