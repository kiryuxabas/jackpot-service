INSERT INTO jackpots (
    jackpot_id,
    initial_pool,
    current_pool,
    contribution_type,
    reward_type,
    contribution_percent,
    contribution_percent_initial,
    contribution_percent_final,
    contribution_pool_limit,
    reward_chance_percent,
    reward_chance_pool_limit
)
VALUES
    ('mega',   1000.00, 1000.00, 'FIXED',    'FIXED',    0.050000, 0.100000, 0.030000, 10000.00, 0.010000, 100000.00),
    ('super',   500.00,  500.00, 'VARIABLE', 'VARIABLE', 0.050000, 0.100000, 0.030000, 10000.00, 0.010000, 100000.00),
    ('medium',  250.00,  250.00, 'VARIABLE', 'FIXED',    0.050000, 0.100000, 0.030000, 10000.00, 0.010000, 100000.00),
    ('mini',    100.00,  100.00, 'FIXED',    'VARIABLE', 0.050000, 0.100000, 0.030000, 10000.00, 0.010000, 100000.00);
