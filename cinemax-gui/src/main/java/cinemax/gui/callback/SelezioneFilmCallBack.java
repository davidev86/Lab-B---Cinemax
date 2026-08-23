

package cinemax.gui.callback;

import cinemax.contracts.dto.FilmDetails;

public interface SelezioneFilmCallBack {

        void onSelezione(FilmDetails filmDetails);
        void offSelezione(String errorMessage);



    }

