classdef Person
    %C Summary of this class goes here
    %   Detailed explanation goes here
    
    properties
        time
        
    end
    
    methods
        function obj = Person()
            obj.time = 0;
        end
        function get = getTime (obj) 
            obj.time = obj.time + 1;
            get = obj.time;
        end
        
    end
    
    methods (Static) 
        function z = zeros(x, y)
            z = zeros(x, y, 'Person');
        end
    end
end

