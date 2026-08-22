package cinemax.gui.callback;

import cinemax.contracts.dto.ui.ProjectionDetailsView;

public interface SelezioneProjectionCallBack {


       void onSelezione(ProjectionDetailsView projectionDetails);
        void offSelezione(String errorMessage);
	
}
