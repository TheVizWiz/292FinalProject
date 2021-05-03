
tau = 14;
gamma = 1/tau;
N = 1;
beta = .1428;


% for i = 0.01:0

ode = @(t, y) [-beta.*y(2).*y(1)./N - 0.02*y(1)*floor(t/15);...
    beta.*y(2).*y(1)/N-gamma.*y(2);...
    gamma.*y(2);...
    0.02.*y(1).*floor(t/150)];

[t, y] = ode45(ode, [0 1000], [1 0.001 0 0]);


hold on
splot = plot(t, y(:, 1), 'LineWidth', 2);
iplot = plot(t, y(:, 2), 'LineWidth', 2);
rplot = plot(t, y(:, 3), 'LineWidth', 2);
legend(["S(t)" "I(t)" "R(t)"]);
f = gcf;
f.Position = [100 100 1920 1080];
ylim([0 1])
xlim([0 500]);

%%
close all
hold on

splot = plot(t, y(:, 1), 'LineWidth', 2);
iplot = plot(t, y(:, 2), 'LineWidth', 2);
rplot = plot(t, y(:, 3), 'LineWidth', 2);
vplot = plot(t, y(:, 4), 'LineWidth', 2);
legend(["S(t)" "I(t)" "R(t)"]);

ylim([0 1])
xlim([0 100]);

vod = VideoWriter("Vaccine Test");
vod.FrameRate = 10;
f = gcf;
f.Position = [100 100 1920 1080];
% b = uicontrol('Parent',f,'Style','slider','Position',[81,54,419,23],...
%               'value',beta, 'min',0, 'max',1);
% b.Callback = @(es, ed) updateGraph(es, ed);
open(vod);
day = 150;
xline(day);
for i = 0.001:0.0001:0.01
    percentvax = i;

    ode = @(t, y) [-beta.*y(2).*y(1)./N - y(1).*percentvax./N.*max(0, (t-day)./400);...
    beta.*y(2).*y(1)/N-gamma.*y(2);...
    gamma.*y(2);...
    y(1).*percentvax./N.*max(0, (t-day)./400)];
%     percentvax.*y(1).*max(0, (1-day)./150)./N];

    
    [t, y] = ode45(ode, [0 1000], [1 0.00000001 0 0]);
    disp(y(:, 4))
%     disp(max(0, (t-150)./150));
%     hold on
    set(splot, "XData", t, "YData", y(:, 1))
    set(iplot, "XData", t, "YData", y(:, 2))
    set(rplot, "XData", t, "YData", y(:, 3))
    set(vplot, "XData", t, "YData", y(:, 4))

    legend(["S(t)" "I(t)" "R(t)" "V(t)"]);
    ylim([0 1])
    xlim([0 inf]);
    pause(0.016);
    frame = getframe(gcf);
    writeVideo(vod, frame);
    title("vaccination rate = " + string(i));
end
close(vod);


function redrawGraph (beta, gamma)
end


