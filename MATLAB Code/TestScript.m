clear all
tau = 21;
gamma = 1/tau;
N = 1;
beta = .14;


% for i = 0.01:0

ode = @(t, y) [-beta.*y(2).*y(1)./N;...
    beta.*y(2).*y(1)/N-gamma.*y(2);...
    gamma.*y(2)];

[t, y] = ode45(ode, [0 10], [1 0.00000001 0]);



%%
close all
hold on
splot = plot(t, y(:, 1), 'LineWidth', 2);
iplot = plot(t, y(:, 2), 'LineWidth', 2);
rplot = plot(t, y(:, 3), 'LineWidth', 2);
legend(["S(t)" "I(t)" "R(t)"]);

ylim([0 1])
xlim([0 100]);

vod = VideoWriter("testing the shizzles");
vod.FrameRate = 10;
f = gcf;
f.Position = [100 100 1920 1080];
% b = uicontrol('Parent',f,'Style','slider','Position',[81,54,419,23],...
%               'value',beta, 'min',0, 'max',1);
% b.Callback = @(es, ed) updateGraph(es, ed);
open(vod);
for i = 0.001:0.01:1
    ode = @(t, y) [-beta.*y(2).*y(1)./N; beta.*y(2).*y(1)/N-gamma.*y(2);...
        gamma.*y(2)];
    
    [t, y] = ode45(ode, [0 1000], [1 0.00000001 0]);
    
%     hold on
    set(splot, "XData", t, "YData", y(:, 1))
    set(iplot, "XData", t, "YData", y(:, 2))
    set(rplot, "XData", t, "YData", y(:, 3))
    legend(["S(t)" "I(t)" "R(t)"]);
    ylim([0 1])
    xlim([0 inf]);
    pause(0.016);
    frame = getframe(gcf);
    writeVideo(vod, frame);
    title("Beta = " + string(i));
end
close(vod);
%%

cloase all
hold on
splot = plot(t, y(:, 1), 'LineWidth', 2);
iplot = plot(t, y(:, 2), 'LineWidth', 2);
rplot = plot(t, y(:, 3), 'LineWidth', 2);
legend(["S(t)" "I(t)" "R(t)"]);
ylim([0 1])
xlim([0 100]);
vod = VideoWriter("gamma variation");
vod.FrameRate = 10;
f = gcf;
f.Position = [100 100 1920 1080];
open(vod);
for i = 0.00001:0.001:0.33
    beta = 0.7;
    gamma = i;
    ode = @(t, y) [-beta.*y(2).*y(1)./N; beta.*y(2).*y(1)/N-gamma.*y(2);...
        gamma.*y(2)];
    
    [t, y] = ode45(ode, [0 1000], [1 0.00000001 0]);
    
%     hold on
    set(splot, "XData", t, "YData", y(:, 1))
    set(iplot, "XData", t, "YData", y(:, 2))
    set(rplot, "XData", t, "YData", y(:, 3))
    legend(["S(t)" "I(t)" "R(t)"]);
    title("gamma = " + string(i));
    ylim([0 1])
    pause(0.016);
    frame = getframe(gcf);
    writeVideo(vod, frame);
    
end
close(vod);


function updateGraph(es, ed)
disp(es.Value)

end