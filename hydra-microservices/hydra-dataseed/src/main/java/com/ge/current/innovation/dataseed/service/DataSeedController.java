package com.ge.current.innovation.dataseed.service;

import com.ge.current.innovation.CsvExternalizationService;
import com.ge.current.innovation.DataPoint;
import com.ge.current.innovation.dataseed.boot.Application;
import com.ge.current.innovation.storage.jpa.dal.AssetMeterRepository;
import com.ge.current.innovation.storage.jpa.dal.DataPointRepository;
import com.ge.current.innovation.storage.jpa.entities.DataPointEntity;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * An example of creating a Rest api using Spring Annotations @RestController.
 *
 *
 *
 * @author predix
 */
@RestController
public class DataSeedController {

    @Autowired
    private CsvExternalizationService csvImportService;

    @Autowired
    private DataPointRepository dataPointRepository;

    @Autowired
    private AssetMeterRepository assetMeterRepository;

    private SimpleDateFormat sdf = new SimpleDateFormat();

    /**
     *
     */
    public DataSeedController() {
        super();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/data/upload")
    public String handleExportedDataFileUpload(@RequestParam("name") String name,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (name.contains("/")) {
            redirectAttributes.addFlashAttribute("message", "Folder separators not allowed");
            return "redirect:upload";
        }
        if (name.contains("/")) {
            redirectAttributes.addFlashAttribute("message", "Relative pathnames not allowed");
            return "redirect:upload";
        }

        if (!file.isEmpty()) {
            try {
                dataPointRepository.save(
                        csvImportService.parse(file.getInputStream(), DataPoint.class).stream()
                        .map((DataPoint dp) -> {
                            Date ts = null;
                            try {
                                ts = sdf.parse(dp.getTs());
                            } catch (ParseException ex) {
                                return null;
                            }
                            return new DataPointEntity(null,
                                    dp.getValue(),
                                    assetMeterRepository.findByUri(dp.getCode()),
                                    ts);
                        }).collect(Collectors.toList()));
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("message", "You failed to upload " + name + " => " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "You failed to upload " + name + " because the file was empty");
        }

        return "redirect:upload";
    }
}
