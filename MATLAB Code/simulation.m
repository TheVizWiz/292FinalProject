close all

data = csvread("simulation.csv");
hold on
plot(data(:, 1), "LineWidth", 2);
plot(data(:, 2), "LineWidth", 2);
plot(data(:, 3), "LineWidth", 2);
plot(data(:, 4), "LineWidth", 2);
legend(["S()" "I(t)" "R(t)" "V(t)"]);


f = gcf;
f.Position = [100 100 1920 1080];

