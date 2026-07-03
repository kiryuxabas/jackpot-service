INSERT INTO jackpots (jackpot_id, initial_pool, current_pool, contribution_type, reward_type)
VALUES
    ('mega',  1000.00, 1000.00, 'FIXED',    'FIXED'),
    ('super',  500.00,  500.00, 'VARIABLE', 'VARIABLE'),
    ('medium', 250.00, 250.00, 'VARIABLE', 'FIXED'),
    ('mini',   100.00,  100.00, 'FIXED',    'VARIABLE');
