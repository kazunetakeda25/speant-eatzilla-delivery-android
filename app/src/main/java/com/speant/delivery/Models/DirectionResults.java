package com.speant.delivery.Models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class DirectionResults {

    @Expose
    public String status;

    @Expose
    public ArrayList<Routes> routes;


    public String getStatus() {
        return status;
    }

    public ArrayList<Routes> getRoutes() {
        return routes;
    }

    public class Routes {

        @Expose
        public ArrayList<Legs> legs;
        public overview_polylines overview_polyline;

        public ArrayList<Legs> getLegs() {
            return legs;
        }
        public overview_polylines getOverview_polyline() {
            return overview_polyline;
        }

    }

    public class Legs {
        @Expose
        public ArrayList<Steps> steps;

        public ArrayList<Steps> getSteps() {
            return steps;
        }
    }

    public class Steps {
        @Expose
        public String html_instructions;

        public PolylineData polyline;

        public String getHtml_instructions() {
            return html_instructions;
        }

        public PolylineData getPolyline() {
            return polyline;
        }

        public class PolylineData {
            public String points;

            public String getPoints() {
                return points;
            }
        }
    }

    public class overview_polylines {

        public String points;

        public String getPoints() {
            return points;
        }
    }
}
