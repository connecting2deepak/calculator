/**
 * 
 */
package com.discount.calculator.Repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.discount.calculator.Entity.ProcessedFiles;

/**
 * @author deepak.s29
 *
 */
public interface ProcessedFilesRepository extends JpaRepository<ProcessedFiles, Long> {

	/**
	 * Find all by file name.
	 *
	 * @param fileName  the file name
	 * @param timeStamp the time stamp
	 * @return the list
	 */
	List<ProcessedFiles> findAllByFileNameAndProcessedDate(final String fileName, final Timestamp timeStamp);

	/**
	 * Find by file name and processed date.
	 *
	 * @param fileName  the file name
	 * @param timeStamp the time stamp
	 * @return the processed files
	 */
	ProcessedFiles findByFileNameAndProcessedDate(final String fileName, final Timestamp timeStamp);

}
