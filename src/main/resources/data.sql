-- Insert Companies
INSERT INTO company (id, name) VALUES
                                   ('550e8400-e29b-41d4-a716-446655440000', 'Société Générale SARL'),
                                   ('550e8400-e29b-41d4-a716-446655440001', 'Innovation Tech SAS'),
                                   ('550e8400-e29b-41d4-a716-446655440002', 'Consulting Plus SA'),
                                   ('550e8400-e29b-41d4-a716-446655440003', 'Digital Solutions EURL'),
                                   ('550e8400-e29b-41d4-a716-446655440004', 'Investment Holdings SCA'),
                                   ('550e8400-e29b-41d4-a716-446655440005', 'Industries Françaises SA');

-- Insert Persons
INSERT INTO person (id, first_name, last_name, national_id, date_of_birth, address) VALUES
                                                                                        ('550e8400-e29b-41d4-a716-446655440006', 'Zoé', 'Dupont', 'FR123456789', '1980-01-01', '123 Rue de Paris, 75001 Paris'),
                                                                                        ('550e8400-e29b-41d4-a716-446655440007', 'Xavier', 'Martin', 'FR987654321', '1975-06-15', '456 Avenue des Champs-Élysées, 75008 Paris'),
                                                                                        ('550e8400-e29b-41d4-a716-446655440008', 'Marie', 'Laurent', 'FR456789123', '1982-03-20', '789 Boulevard Haussmann, 75009 Paris'),
                                                                                        ('550e8400-e29b-41d4-a716-446655440009', 'Pierre', 'Bernard', 'FR789123456', '1978-11-30', '321 Rue du Commerce, 75015 Paris'),
                                                                                        ('550e8400-e29b-41d4-a716-446655440010', 'Sophie', 'Dubois', 'FR321654987', '1985-07-25', '654 Avenue Montaigne, 75008 Paris'),
                                                                                        ('550e8400-e29b-41d4-a716-446655440011', 'Jean', 'Moreau', 'FR654987321', '1973-09-12', '987 Rue de Rivoli, 75001 Paris');


-- Direct ownership example
INSERT INTO ownership (id, company_id, owner_id, owner_type, percentage) VALUES
    ('550e8400-e29b-41d4-a716-446655440012', '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440006', 'Person', 30.0);

-- Multiple owners for one company
INSERT INTO ownership (id, company_id, owner_id, owner_type, percentage) VALUES
                                                                             ('550e8400-e29b-41d4-a716-446655440013', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440007', 'Person', 20.0),
                                                                             ('550e8400-e29b-41d4-a716-446655440014', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440008', 'Person', 15.0),
                                                                             ('550e8400-e29b-41d4-a716-446655440015', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440009', 'Person', 35.0);

-- Indirect ownership companies
INSERT INTO ownership (id, company_id, owner_id, owner_type, percentage) VALUES
                                                                             ('550e8400-e29b-41d4-a716-446655440016', '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440001', 'Company', 60.0),
                                                                             ('550e8400-e29b-41d4-a716-446655440017', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440010', 'Person', 30.0);

-- Circular ownership
INSERT INTO ownership (id, company_id, owner_id, owner_type, percentage) VALUES
                                                                             ('550e8400-e29b-41d4-a716-446655440018', '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440004', 'Company', 40.0),
                                                                             ('550e8400-e29b-41d4-a716-446655440019', '550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440003', 'Company', 30.0),
                                                                             ('550e8400-e29b-41d4-a716-446655440020', '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440011', 'Person', 45.0);

-- Complex chain of ownership
INSERT INTO ownership (id, company_id, owner_id, owner_type, percentage) VALUES
                                                                             ('550e8400-e29b-41d4-a716-446655440021', '550e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440002', 'Company', 50.0),
                                                                             ('550e8400-e29b-41d4-a716-446655440022', '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440000', 'Company', 40.0),
                                                                             ('550e8400-e29b-41d4-a716-446655440023', '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440010', 'Person', 55.0);