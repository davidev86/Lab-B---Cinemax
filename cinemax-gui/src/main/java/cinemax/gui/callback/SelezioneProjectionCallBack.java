package cinemax.gui.callback;

import cinemax.application.services.ProjectionService;
import cinemax.application.services.TcpClient;
import cinemax.contracts.dto.ProjectionDetails;

public interface SelezioneProjectionCallBack {


       void onSelezione(ProjectionDetails projectionDetails);
        void offSelezione(String errorMessage);
	
}
