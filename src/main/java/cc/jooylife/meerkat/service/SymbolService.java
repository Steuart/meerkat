package cc.jooylife.meerkat.service;

import cc.jooylife.meerkat.repository.dao.SymbolDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SymbolService {

    @Autowired
    private SymbolDao symbolDao;


}
